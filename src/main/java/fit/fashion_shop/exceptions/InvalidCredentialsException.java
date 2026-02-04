package fit.fashion_shop.exceptions;

// Lỗi sai thông tin đăng nhập
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
