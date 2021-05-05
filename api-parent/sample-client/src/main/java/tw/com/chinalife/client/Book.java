package tw.com.chinalife.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Book", description = "這是Book的描述")
public class Book {
	@Schema(description = "這是Book的Data", maxLength = 20)
	private String data;
}
