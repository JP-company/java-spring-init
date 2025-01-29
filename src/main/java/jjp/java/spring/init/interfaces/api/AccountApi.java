package jjp.java.spring.init.interfaces.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDateTime;
import jjp.java.spring.init.app.service.AccountService;
import jjp.java.spring.init.app.viewmodel.AccountGetOneViewModel;
import jjp.java.spring.init.interfaces.dto.request.GetAccountParam;
import jjp.java.spring.init.interfaces.dto.request.PostAccountBody;
import jjp.java.spring.init.interfaces.dto.response.GetAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountApi {

  private final AccountService accountService;

  @GetMapping("")
  @Operation(summary = "계정 조회")
  public GetAccountResponse getAccount(
      @Parameter GetAccountParam param
  ) {
    AccountGetOneViewModel viewModel = this.accountService.getOne(param.accountId());
    return new GetAccountResponse(viewModel);
  }

  @PostMapping("")
  @Operation(summary = "계정 등록")
  public void postAccount(
      @RequestBody PostAccountBody body
  ) {
    LocalDateTime now = LocalDateTime.now();
    this.accountService.register(body.address(), now);
  }
}
