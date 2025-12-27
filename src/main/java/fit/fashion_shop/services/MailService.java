/*
 * @ (#) MailService.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
 * @description:
 * @author: Bao Thong
 * @date: 27/12/2025
 * @version: 1.0
 */

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${mail.from.address}")
    private String fromEmail;

    public void sendOtpMail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Xác thực tài khoản Fashion Shop của bạn");

            // Tạo nội dung HTML
            String htmlContent = buildOtpEmailTemplate(otp);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi khi gửi mail: " + e.getMessage());
        }
    }

    private String buildOtpEmailTemplate(String otp) {
        return """
        <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #eee; border-radius: 10px; overflow: hidden;">
            <div style="background-color: #000; padding: 20px; text-align: center;">
                <h1 style="color: #fff; margin: 0; letter-spacing: 5px; text-transform: uppercase;">Fashion Shop</h1>
            </div>
            <div style="padding: 40px 20px; text-align: center; color: #333;">
                <h2 style="margin-bottom: 20px;">Xác Thực Đăng Ký</h2>
                <p style="font-size: 16px; line-height: 1.6;">Chào mừng bạn đến với thế giới thời trang của chúng tôi! Để hoàn tất đăng ký, vui lòng sử dụng mã OTP dưới đây:</p>
                <div style="margin: 30px 0;">
                    <span style="font-size: 32px; font-weight: bold; letter-spacing: 10px; padding: 10px 20px; border: 2px dashed #000; background-color: #f9f9f9;">
                        %s
                    </span>
                </div>
                <p style="font-size: 14px; color: #777;">Mã này sẽ hết hạn sau <b>5 phút</b>. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>
            </div>
            <div style="background-color: #f4f4f4; padding: 20px; text-align: center; font-size: 12px; color: #888;">
                <p style="margin: 5px 0;">© 2025 Fashion Shop. All rights reserved.</p>
                <p style="margin: 5px 0;">Địa chỉ: TP.HCM</p>
            </div>
        </div>
        """.formatted(otp);
    }

    public void sendForgotPasswordMail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Đặt lại mật khẩu Fashion Shop");

            String htmlContent = buildResetPasswordEmailTemplate(otp);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi khi gửi mail: " + e.getMessage());
        }
    }

    private String buildResetPasswordEmailTemplate(String otp) {
        return """
    <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #eee; border-radius: 10px;">
        <div style="background-color: #000; padding: 20px; text-align: center;">
            <h1 style="color: #fff; margin: 0; letter-spacing: 5px;">Fashion Shop</h1>
        </div>
        <div style="padding: 40px 20px; text-align: center; color: #333;">
            <h2>Yêu Cầu Đặt Lại Mật Khẩu</h2>
            <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Vui lòng sử dụng mã OTP dưới đây:</p>
            <div style="margin: 30px 0;">
                <span style="font-size: 32px; font-weight: bold; letter-spacing: 10px; padding: 10px 20px; border: 2px dashed #000; background-color: #f9f9f9;">
                    %s
                </span>
            </div>
            <p style="font-size: 14px; color: #777;">Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email này.</p>
        </div>
    </div>
    """.formatted(otp);
    }
}
