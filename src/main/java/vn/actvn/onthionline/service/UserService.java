package vn.actvn.onthionline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.client.dto.*;
import vn.actvn.onthionline.common.ValidationError;
import vn.actvn.onthionline.common.exception.ServiceException;
import vn.actvn.onthionline.common.exception.ServiceExceptionBuilder;
import vn.actvn.onthionline.common.exception.ValidationErrorResponse;
import vn.actvn.onthionline.common.utils.ServiceUtil;
import vn.actvn.onthionline.domain.User;
import vn.actvn.onthionline.repository.UserRepository;
import vn.actvn.onthionline.service.dto.UserProfileDto;
import vn.actvn.onthionline.service.mapper.UserProfileMapper;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private ImageService imageService;

    public UploadImgProfileResponse uploadImgProfile(UploadImgProfileRequest request, String username) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getImgBase64())
            throw ServiceExceptionBuilder.newBuilder()
                    .addError(new ValidationErrorResponse("Img Base64", ValidationError.NotEmpty))
                    .build();
            if (null == request.getFileTail())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("File tail", ValidationError.NotEmpty))
                        .build();

            Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
            if (!user.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Something was", ValidationError.Wrong))
                        .build();

            // Save url to database
            String filePath = imageService.saveFile(request.getImgBase64(), username, request.getFileTail());

            if (null != filePath) {
                user.get().setAvatar(filePath);
                userRepository.save(user.get());
            }
            return new UploadImgProfileResponse(true);
        } catch (ServiceException e) {
            throw e;
        } catch (IOException e) {
            throw ServiceExceptionBuilder.newBuilder()
                    .addError(new ValidationErrorResponse("Something was", ValidationError.Wrong))
                    .build();
        }
    }

    public GetImgProfileResponse getImgProfile(String username) throws ServiceException {
        try {
            Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
            if (!user.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Something was", ValidationError.Wrong))
                        .build();

            String imgBase64 = null;

            if (null != user.get().getAvatar())
                imgBase64 = imageService.getFile(user.get().getAvatar());

            GetImgProfileResponse response = new GetImgProfileResponse(imgBase64);
            return response;
        } catch (ServiceException e) {
            throw e;
        } catch (IOException e) {
            throw ServiceExceptionBuilder.newBuilder()
                    .addError(new ValidationErrorResponse("Something was", ValidationError.Wrong))
                    .build();
        }
    }

    public GetProfileInfoResponse getProfileInfo(String username) throws ServiceException {
        try {
            Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
            if (!user.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Something was", ValidationError.Wrong))
                        .build();

            UserProfileDto userDto = userProfileMapper.toDto(user.get());
            GetProfileInfoResponse response = new GetProfileInfoResponse();
            response.setUserProfile(userDto);
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public UpdateProfileInfoResponse updateProfile(UpdateProfileInfoRequest request, String username) throws ServiceException{
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getUserProfile())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Userprofile", ValidationError.NotEmpty))
                        .build();
            Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
            if (!user.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Something was", ValidationError.Wrong))
                        .build();


            User updateUser = userProfileMapper.toEntity(request.getUserProfile());
            updateUser.setId(user.get().getId());
            updateUser.setEmail(user.get().getEmail());
            updateUser.setUsername(user.get().getUsername());
            updateUser.setPassword(user.get().getPassword());
            updateUser.setCreatedDate(user.get().getCreatedDate());
            updateUser.setAvatar(user.get().getAvatar());
            updateUser.setLastLogin(user.get().getLastLogin());
            updateUser.setOnlineTime(user.get().getOnlineTime());
            updateUser.setIsActive(user.get().getIsActive());
            updateUser.setLastLogin(user.get().getLastLogin());
            updateUser.setRoles(user.get().getRoles());
            updateUser.setExamHistories(user.get().getExamHistories());

            Optional<User> updated = Optional.of(
                    Optional.of(userRepository.saveAndFlush(updateUser)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(userUpdate -> {
                        LOGGER.debug("Updated Information for User: {}", userUpdate);
                        return userUpdate;
                    });

            if (!updated.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Update user","failed"))
                        .build();

            UpdateProfileInfoResponse response = new UpdateProfileInfoResponse();
            response.setUserProfile(userProfileMapper.toDto(updated.get()));
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request, String username) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getOldPassword())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Old password", ValidationError.NotEmpty))
                        .build();
            if (null == request.getNewPassword())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("New password", ValidationError.NotEmpty))
                        .build();
            Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
            if (!user.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Something was", ValidationError.Wrong))
                        .build();
            if (!bcryptEncoder.matches(request.getOldPassword(), user.get().getPassword()))
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Old password", ValidationError.Invalid))
                        .build();

            user.get().setPassword(bcryptEncoder.encode(request.getNewPassword()));
            Optional<User> updated = Optional.of(
                    Optional.of(userRepository.saveAndFlush(user.get())))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(userUpdate -> {
                        LOGGER.debug("Change Password for User: {}", userUpdate);
                        return userUpdate;
                    });
            if (!updated.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Change password","failed"))
                        .build();

            ChangePasswordResponse response = new ChangePasswordResponse();
            response.setSuccess(true);
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }
}
