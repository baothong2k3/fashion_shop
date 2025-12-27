package fit.fashion_shop.exceptions;

// Lỗi chưa kích hoạt tài khoản
public class AccountNotEnabledException extends RuntimeException {
    public AccountNotEnabledException(String message) {
        super(message);
    }
}
