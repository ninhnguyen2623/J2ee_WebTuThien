package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Users.UserAddDto;
import com.example.springdonateweb.Models.Dtos.Users.UserCreateDto;
import com.example.springdonateweb.Models.Dtos.Users.UserUpdateDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Models.Entities.UsersEntity;
import com.example.springdonateweb.Repositories.UsersRepository;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import com.example.springdonateweb.Services.mappers.UsersMapper;
import com.example.springdonateweb.util.AppUtil;
import com.example.springdonateweb.util.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService implements IUsersService {


    private final PasswordEncoder passwordEncoder;

    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;

    private final HttpSession session;

    private final AppUtil appUtil;

    private final EmailService emailService;

    @Value("${pass.length-of-pass}")
    public Integer lengthOfPass;

    @Override
    public List<UsersResponseDto> findAll() {
        return usersRepository.findAll().stream().map(usersMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public UsersResponseDto findById(int id) {
        return null;
    }

    private Optional<UsersEntity> findUserByIdAndStatusTrue(int id) {
        return usersRepository.findByIdAndStatusTrue(id);
    }


    @Override
    public UsersResponseDto update(UserAddDto userAddDto) {
        Optional<UsersEntity> usersEntity = findUserByIdAndStatusTrue(userAddDto.getId());
        return usersEntity.map(user -> {
            user.setName(userAddDto.getName());
            user.setEmail(userAddDto.getEmail());
            user.setRoleId(userAddDto.getRoleId());
            UsersEntity result = usersRepository.save(user);
            return usersMapper.toResponseDto(result);
        }).orElse(null);
    }


    @Override
    @Transactional
    public UsersResponseDto delete(int id) {
        Optional<UsersEntity> usersEntity = usersRepository.findByIdAndStatusTrue(id);
        return usersEntity
                .map(user -> {
                    user.setStatus(false);
                    return usersMapper.toResponseDto(usersRepository.save(user));
                })
                .orElse(null);
    }

    @Override
    public boolean existsById(int id) {
        return usersRepository.existsById(id);
    }


    public UsersResponseDto findByEmail(String email) {
        Optional<UsersEntity> usersEntity = usersRepository.findByEmail(email);
        return usersEntity.map(usersMapper::toResponseDto).orElse(null);
    }


    @Override
    public UsersResponseDto register(UserCreateDto userCreateDto) {
        UsersEntity usersEntity = usersMapper.toEntity(userCreateDto);
        usersEntity.setPassword(passwordEncoder.encode(usersEntity.getPassword()));
        usersEntity.setStatus(true);
        usersEntity.setRoleId(2);
        UsersEntity result = usersRepository.save(usersEntity);
        UserDetails userDetails = createUserDetailFromRegister(result);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // Lưu Authentication vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
        return usersMapper.toResponseDto(result);
    }

    @Override
    public UsersResponseDto update(UserUpdateDto userUpdateDto) {
        return null;
    }

    @Override
    public UserDetails createUserDetailFromRegister(UsersEntity usersEntity) {
        return new org.springframework.security.core.userdetails.User(
                usersEntity.getEmail(),
                usersEntity.getPassword(),
                List.of(new SimpleGrantedAuthority(usersEntity.getRoleId().toString()))
        );
    }

    @Override
    public List<UsersResponseDto> findByStatusTrue() {
        return usersRepository.findByStatusTrue().stream()
                .map(usersMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UsersResponseDto> findUsersByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UsersEntity> userPage = usersRepository.findAll(pageable);
        return userPage.map(usersMapper::toResponseDto);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public boolean checkPassword(int id, String password) {
        Optional<UsersEntity> usersEntity = usersRepository.findByIdAndStatusTrue(id);
        if (usersEntity.isPresent()) {
            UsersEntity user = usersEntity.get();
            return passwordEncoder.matches(password, user.getPassword());

        } else {
            return false;
        }
    }

    @Override
    public boolean checkComfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    @Override
    @Transactional
    public void changePassword(int id, String password) {
        Optional<UsersEntity> usersEntity = usersRepository.findByIdAndStatusTrue(id);
        usersEntity.ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(password));
            usersRepository.save(user);
        });
    }


    @Override
    public UsersResponseDto findByIdAndStatusTrue(int id) {
        Optional<UsersEntity> usersEntity = usersRepository.findByIdAndStatusTrue(id);
        return usersEntity.map(usersMapper::toResponseDto).orElse(null);
    }

    @Override
    public UsersResponseDto findByEmailAndStatusTrue(String email) {
        Optional<UsersEntity> usersEntity = usersRepository.findByEmailAndStatusTrue(email);
        return usersEntity.map(usersMapper::toResponseDto).orElse(null);
    }

    @Override
    public UsersResponseDto create(UserAddDto userAddDto) {
        UsersEntity usersEntity = usersMapper.toEntity(userAddDto);
        usersEntity.setPassword(passwordEncoder.encode(usersEntity.getPassword()));
        usersEntity.setStatus(true);
        UsersEntity result = usersRepository.save(usersEntity);
        return usersMapper.toResponseDto(result);
    }

    @Override
    @Transactional
    public void resetPassword(String email) {
        Optional<UsersEntity> personEntity = usersRepository.findByEmailAndStatusTrue(email);
        personEntity.ifPresent(person -> {
            String rawPass = appUtil.generateRandomString(lengthOfPass);
            person.setPassword(passwordEncoder.encode(rawPass));
            try {
                emailService.sendEmail(
                        person.getEmail(),
                        "Cấp lại mật khẩu",
                        "Mật khẩu mới của bạn là " + rawPass + " , vui lòng không chia sẽ mật khẩu này cho bất kì ai!!"
                );
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Override
    public void sendOtp(int id, String email) {
        try {
            String otp = appUtil.generateOtp(6);
            emailService.sendEmail(
                    email,
                    "OTP",
                    "Mã OTP của bạn là: " + otp
            );
            Optional<UsersEntity> usersEntity = usersRepository.findByIdAndStatusTrue(id);
            usersEntity.ifPresent(user -> {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                String formattedDate = LocalDateTime.now().plusMinutes(5).format(formatter);
                user.setChangeEmail(email + ";" + otp + ";" + formattedDate);
                usersRepository.save(user);
            });
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void changeEmail(int id) {
        Optional<UsersEntity> usersOptional = usersRepository.findByIdAndStatusTrue(id);
        usersOptional.ifPresent(user -> {
            String[] data = user.getChangeEmail().split(";");
            user.setEmail(data[0]);
            usersRepository.save(user);

        });
    }

    @Override
    public boolean checkOtp(int id, String otp) {
        Optional<UsersEntity> getUser = usersRepository.findByIdAndStatusTrue(id);
        if (getUser.isPresent()) {
            UsersEntity usersEntity = getUser.get();
            String[] data = usersEntity.getChangeEmail().split(";");
            if (otp.equals(data[1])) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                LocalDateTime expiryDate = LocalDateTime.parse(data[2], formatter);
                return LocalDateTime.now().isBefore(expiryDate);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
