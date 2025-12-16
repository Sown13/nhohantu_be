package com.nhohantu.tcbookbe.common.service;

import com.nhohantu.tcbookbe.business.dto.OrderMailItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPaymentSuccessMail(
            String to,
            String orderCode,
            BigDecimal amount,
            List<OrderMailItem> items
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Thanh toán thành công – Đơn " + orderCode);

            helper.setText(buildHtml(orderCode, amount, items), true);

            mailSender.send(message);

        } catch (MessagingException e) {
            // không làm hỏng flow thanh toán
            log.error("Send payment success mail failed", e);
        }
    }

    private String buildHtml(
            String orderCode,
            BigDecimal amount,
            List<OrderMailItem> items
    ) {
        String rows = items.stream()
                .map(i -> """
                <tr>
                  <td style="padding:8px;border:1px solid #ddd">%s</td>
                  <td style="padding:8px;border:1px solid #ddd;text-align:center">%d</td>
                  <td style="padding:8px;border:1px solid #ddd;text-align:right">%s VND</td>
                </tr>
            """.formatted(
                        i.getProductName(),
                        i.getQuantity(),
                        i.getPrice().toPlainString()
                ))
                .collect(Collectors.joining());

        return """
        <div style="font-family: Arial, sans-serif; font-size:14px">
          <h3>Thanh toán thành công</h3>

          <p>Mã đơn hàng: <b>%s</b></p>

          <table style="border-collapse:collapse;width:100%%;margin-top:10px">
            <thead>
              <tr style="background:#f5f5f5">
                <th style="padding:8px;border:1px solid #ddd;text-align:left">Sản phẩm</th>
                <th style="padding:8px;border:1px solid #ddd">Số lượng</th>
                <th style="padding:8px;border:1px solid #ddd;text-align:right">Giá</th>
              </tr>
            </thead>
            <tbody>
              %s
            </tbody>
          </table>

          <p style="margin-top:12px">
            <b>Tổng thanh toán: %s VND</b>
          </p>

          <p>Cảm ơn bạn đã sử dụng dịch vụ.</p>
        </div>
    """.formatted(orderCode, rows, amount.toPlainString());
    }

}

