package pl.pjatk.jaz_s32698_nbp.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleNbpErrors(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("404: Brak danych (waluta nie istnieje lub brak kursów w podanym przedziale).");
        }
        if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("400: NBP odrzucił zapytanie (np. limit maksymalnie 367 dni w przedziale).");
        }
        return ResponseEntity.status(ex.getStatusCode()).body("Błąd NBP: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchError(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błędne żądanie: Upewnij się, że format daty to YYYY-MM-DD.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentError(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Błąd: " + ex.getMessage());
    }
}