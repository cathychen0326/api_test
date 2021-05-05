package tw.com.chinalife.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import tw.com.chinalife.api.client.ApiClient;
import tw.com.chinalife.api.client.model.ApiClientResponse;
import tw.com.chinalife.api.model.ApiMessage;
import tw.com.chinalife.api.model.example.ApiRequestSample;
import tw.com.chinalife.api.model.example.ApiRequestSample2;
import tw.com.chinalife.api.model.example.ReturnObjectSample;
import tw.com.chinalife.api.model.example.ReturnObjectSample2;

@RestController
@RequestMapping("api")
@Slf4j
public class RestResource {

	@Autowired
	private ApiClient apiClient;

	@GetMapping("/{id}")
	public ApiMessage findById(@PathVariable long id) throws InterruptedException, ExecutionException {
		ApiRequestSample sample;
		if (id == 1) {
			// 正常回應 對應 sample-server: MyController.handleDefault
			sample = ApiRequestSample.builder().message("success").build();
		} else if (id == 0) {
			// 丟出正常ApiException測試 對應 sample-server: MyController.handleDefault
			sample = ApiRequestSample.builder().message("丟出正常ApiException測試").build();
		} else if (id == 2) {
			// 欄位檢核測試
			sample = ApiRequestSample.builder().message("").build();
		} else {
			// 不可預期的Exception測試 對應 sample-server: MyController.handleDefault
			sample = ApiRequestSample.builder().message("runtime").build();
		}
		CompletableFuture<ApiClientResponse<ReturnObjectSample>> message = apiClient.getMessage(sample);
		ApiRequestSample2 sample2 = ApiRequestSample2.builder().message("testssss").build();
		// 正常回應 對應 sample-server: MyController.handleDefault2
		CompletableFuture<ApiClientResponse<ReturnObjectSample2>> message2 = apiClient.getMessage(sample2);
		log.info("===========my=====message2============" + message2.get().getResult().getMyMessage());
		if (message.get().isSuccess()) {
			log.info("===========my=====message============" + message.get().getResult().getMyMessage());
			return message.get();
		} else {
			log.info("===========my===eeor==message============" + message.get().getErrorResponse().getMessage());
			return message.get().getErrorResponse();
		}
	}

	@PostMapping("/")
	@Operation(summary = "Post Book", description = "這是Book描述")
	public ResponseEntity<Book> findById(@RequestBody Book book) {
		Book b = new Book();
		log.debug("data=== {}", book.getData());
		BeanUtils.copyProperties(book, b);
		return ResponseEntity.ok(b);
	}

}
