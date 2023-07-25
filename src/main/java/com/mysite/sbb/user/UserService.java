package com.mysite.sbb.user;

import com.mysite.sbb.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }

    public SiteUser create(String username, String email, String password,String providerTypeCode) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setProviderTypeCode(providerTypeCode);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }


    public Optional<SiteUser> findUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public SiteUser whenSocialLogin(String providerTypeCode, String username) {
        Optional<SiteUser> opMember = findUser(username); // username 예시 : KAKAO__1312319038130912, NAVER__1230812300

        if (opMember.isPresent()) return opMember.get();

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return create(providerTypeCode, username, ""); // 최초 로그인 시 딱 한번 실행
    }
}
