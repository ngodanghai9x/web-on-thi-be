package vn.actvn.onthionline.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.client.dto.*;
import vn.actvn.onthionline.common.Constant;
import vn.actvn.onthionline.common.ValidationError;
import vn.actvn.onthionline.common.exception.ServiceException;
import vn.actvn.onthionline.common.exception.ServiceExceptionBuilder;
import vn.actvn.onthionline.common.exception.ValidationErrorResponse;
import vn.actvn.onthionline.common.utils.ServiceUtil;
import vn.actvn.onthionline.domain.Role;
import vn.actvn.onthionline.domain.User;
import vn.actvn.onthionline.repository.RoleRepository;
import vn.actvn.onthionline.repository.UserRepository;
import vn.actvn.onthionline.service.dto.EmailDto;
import vn.actvn.onthionline.service.mapper.UserRegisterMapper;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRegisterMapper userRegisterMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtTokenService jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                AuthorityUtils.createAuthorityList(Constant.ROLE_USER));
    }


    public LoginResponse generateToken(LoginRequest request, HttpServletRequest httpRequest) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getUsername())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Username", ValidationError.NotNull))
                        .build();
            if (null == request.getPassword() && null == request.getOtp())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Password or Otp", ValidationError.NotNull))
                        .build();

            User user = userRepository.findByUsername(request.getUsername());
            if (null == user)
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Username or password/ otp", ValidationError.Invalid))
                        .build();

            if (user.getIsActive() == false)
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("User", ValidationError.Disabled))
                        .build();

            if (null != request.getPassword()) {
                if (!bcryptEncoder.matches(request.getPassword(), user.getPassword()))
                    throw ServiceExceptionBuilder.newBuilder()
                            .addError(new ValidationErrorResponse("Username or password/ otp", ValidationError.Invalid))
                            .build();
            } else {
                if (!otpService.validateOTP(request.getUsername() + Constant.LOGIN, request.getOtp())) {
                    throw ServiceExceptionBuilder.newBuilder()
                            .addError(new ValidationErrorResponse("Username or password/ otp", ValidationError.Invalid))
                            .build();
                }
            }

            //Update last login
            user.setLastLogin(new Date());
            userRepository.save(user);

            LoginResponse response = new LoginResponse();
            response.setToken(jwtTokenUtil.generate(user, httpRequest));

            return response;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    //save new user
    public RegisterResponse register(RegisterRequest request) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getUserRegister())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("User Register", ValidationError.NotNull))
                        .build();


            Optional<User> user = Optional.ofNullable(userRepository.findByUsername(request.getUserRegister().getUsername()));
            if (user.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Username", ValidationError.Exists))
                        .build();

            Integer emailExist = userRepository.countByEmail(request.getUserRegister().getEmail());
            if (emailExist > 0)
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Email", ValidationError.Exists))
                        .build();

            User newUser = userRegisterMapper.toEntity(request.getUserRegister());
            newUser.setPassword(bcryptEncoder.encode(request.getUserRegister().getPassword()));
            newUser.setCreatedDate(new Date());
            newUser.setIsActive(true);
            newUser.setOnlineTime(0);
            Set<Role> roleList = new HashSet<>();
            roleList.add(roleRepository.findByRoleName(Constant.ROLE_USER));
            newUser.setRoles(roleList);

            RegisterResponse response = new RegisterResponse();
            User saveUser = userRepository.save(newUser);

            if (null != saveUser) {
                response.setSuccess(true);
            } else response.setSuccess(false);

            return response;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public GetOTPResponse generateOtp(GetOTPRequest request, String purpose) throws ServiceException, MessagingException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getUsername() && null == request.getEmail())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Username or email", ValidationError.NotNull))
                        .build();
            if (null != request.getUsername() && null != request.getEmail()) {
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("You must be use", "username or email"))
                        .build();
            }

            Optional<User> user = null;
            if (null != request.getUsername()) {
                user = Optional.ofNullable(userRepository.findByUsername(request.getUsername()));
                if (!user.isPresent())
                    throw ServiceExceptionBuilder.newBuilder()
                            .addError(new ValidationErrorResponse("Username", ValidationError.Invalid))
                            .build();
            }

            if (null != request.getEmail()) {
                user = Optional.ofNullable(userRepository.findByEmail(request.getEmail()));
                if (!user.isPresent())
                    throw ServiceExceptionBuilder.newBuilder()
                            .addError(new ValidationErrorResponse("Email", ValidationError.Invalid))
                            .build();
            }

            Integer otpValue = otpService.generateOtp(user.get().getUsername() + (purpose.equals(Constant.OTP_LOGIN) ? Constant.LOGIN : Constant.FORGOT_PASSWORD));

            // Sent mail
            if ( -1 != otpValue) {
                // fetch user e-mail from database
                List<String> recipients = new ArrayList<>();
                recipients.add(user.get().getEmail());

                // generate emailDTO object
                EmailDto emailDTO = new EmailDto();
                emailDTO.setSubject("WebOnThi - Thi trắc nghiệm trực tuyến miễn phí 2020");
                emailDTO.setBody("<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\"bgcolor=\"#f0f0f0\">\n" +
                        "        <tr>\n" +
                        "        <td style=\"padding: 30px 30px 20px 30px;\">\n" +
                        "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" bgcolor=\"#ffffff\" style=\"max-width: 650px; margin: auto;\">\n" +
                        "            <tr>\n" +
                        "                <td colspan=\"2\" align=\"center\" style=\"background-color: #f3f8fd; padding: 40px;\">\n" +
                        "                    <a href=\"http://webonthi.com/\" target=\"_blank\"><img src=\"https://hoctot.com/images/logo.png\" border=\"0\" /></a>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td colspan=\"2\" align=\"center\" style=\"padding: 50px 50px 0px 50px;\">\n" +
                        "                    <h1 style=\"padding-right: 0em; margin: 0; line-height: 40px; font-weight:300; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 1em;\">\n" +
                        "                        Email OTP\n" +
                        "                    </h1>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"text-align: left; padding: 0px 50px;\" valign=\"top\">\n" +
                        "                    <p style=\"font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 3%;\">\n" +
                        "                        Chào bạn " + user.get().getFullname() + ", \n" +
                        "                    </p>\n" +
                        "                    <p style=\"font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 3%;\">\n" +
                        "                        ĐỂ " + purpose + ", mã xác minh là <span style=\"background-color: #F0F0F0; font-size: 20px; color: #3DA9F5\">" + otpValue + "</span>. Có hiệu lực trong 5 phút. KHÔNG chia sẻ mã này cho bất kì ai khác.\n" +
                        "                    </p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"text-align: left; padding: 30px 50px 50px 50px\" valign=\"top\">\n" +
                        "                    <p style=\"font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #505050; text-align: left;\">\n" +
                        "                        Thanks,<br/>WebOnThi Team\n" +
                        "                    </p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td colspan=\"2\" align=\"center\" style=\"padding: 20px 40px 40px 40px;\" bgcolor=\"#f0f0f0\">\n" +
                        "                    <p style=\"font-size: 12px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #777;\">\n" +
                        "                        &copy; 2020\n" +
                        "                        <a href=\"http://webonthi.com/\" target=\"_blank\" style=\"color: #777; text-decoration: none\">Webonthi.com</a>\n" +
                        "                        <br>\n" +
                        "                        141 Chiến Thắng, Xã Tân Triều, H. Thanh Trì, Tp. Hà Nội.\n" +
                        "                    </p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            </table>\n" +
                        "        </td>\n" +
                        "    </tr>\n" +
                        "</table>");
                emailDTO.setRecipients(recipients);
                emailDTO.setIsHtml(true);

                // send generated e-mail
                emailService.sendMessage(emailDTO);
                return new GetOTPResponse(true);
            }
            else
                return new GetOTPResponse(false);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public ChangeForgotPassResponse changeForgotPassword(ChangeForgotPassRequest request) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getOtp())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Otp", ValidationError.NotNull))
                        .build();
            if (null == request.getUsername() && null == request.getEmail())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Username or email", ValidationError.NotNull))
                        .build();
            if (null != request.getUsername() && null != request.getEmail()) {
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("You must be use", "username or email"))
                        .build();
            }
            if (null != request.getEmail()) {
                Optional<User> user = Optional.ofNullable(userRepository.findByEmail(request.getEmail()));
                if (user.isPresent()) request.setUsername(user.get().getUsername());
            }

            if (true == otpService.validateOTP(request.getUsername() + Constant.FORGOT_PASSWORD, request.getOtp())) {
                User user = userRepository.findByUsername(request.getUsername());
                user.setPassword(bcryptEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
                return new ChangeForgotPassResponse(true);
            } else {
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("OTP", ValidationError.Invalid))
                        .build();
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

}