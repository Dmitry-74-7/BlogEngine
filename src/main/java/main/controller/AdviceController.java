package main.controller;


import main.api.response.ImageResponse.ImageResponse;
import main.api.response.ImageResponse.ErrorImage;
import main.api.response.myProfileResponse.MyProfileErrResponse;
import main.api.response.myProfileResponse.MyProfileError;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AdviceController {
  @ExceptionHandler(FileSizeLimitExceededException.class)
  public ResponseEntity exceptionSize(WebRequest request) {
    ErrorImage err = new ErrorImage("Превышен размер изображения!");
    String requestType = request.getDescription(false);

    if (requestType.equals("uri=/api/profile/my") ) {
      MyProfileError myProfileError = new MyProfileError();
      myProfileError.setPhoto("Превышен размер в 5Мб");
      MyProfileErrResponse myProfileErrResponse = new MyProfileErrResponse();
      myProfileErrResponse.setResult(false);
      myProfileErrResponse.setErrors(myProfileError);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(myProfileErrResponse);
    }

    if (requestType.equals("uri=/api/image")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImageResponse(false, err));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
  }
}
