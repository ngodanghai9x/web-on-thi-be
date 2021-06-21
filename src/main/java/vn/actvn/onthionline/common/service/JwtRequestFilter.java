package vn.actvn.onthionline.common.service;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.actvn.onthionline.common.Constant;
import vn.actvn.onthionline.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
    @Autowired
    private JwtTokenService jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(Constant.AUTHORIZATION);
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith(Constant.BEARER)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenProvider.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                LOGGER.info("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                LOGGER.info("JWT Token has expired");
            }
        } else {
            LOGGER.info("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userRepository.findByUsername(username);
            if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {
                final Claims claims = jwtTokenProvider.getAllClaimsFromToken(jwtToken);

                if (!jwtTokenProvider.getClientIp(request).equals(claims.get(Constant.IP))) {
                    LOGGER.info("Token of username: '{}' has another ip", jwtTokenProvider.getUsernameFromToken(jwtToken));
                    throw new AccessDeniedException(null);
                }

                if (!jwtTokenProvider.getUserAgent(request).equals(claims.get(Constant.USER_AGENT))) {
                    LOGGER.info("Token of username: '{}' has another user_agent", jwtTokenProvider.getUsernameFromToken(jwtToken));
                    throw new AccessDeniedException(null);
                }


                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}

