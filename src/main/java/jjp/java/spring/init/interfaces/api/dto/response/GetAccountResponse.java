package jjp.java.spring.init.interfaces.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import jjp.java.spring.init.app.viewmodel.AccountGetOneViewModel;
import jjp.java.spring.init.domain.model.type.AccountStatus;

public record GetAccountResponse(
    @Schema(example = "1")
    int accountId,

    @Schema(description = "이름", example = "전정표")
    String name,

    @Schema(description = "계정 상태", example = "ACTIVE")
    AccountStatus status,

    @Schema(description = "계성 생성 시간", example = "2024-10-10")
    LocalDateTime createdAt
) {

  public GetAccountResponse(AccountGetOneViewModel viewModel) {
    this(
        viewModel.getAccountId(),
        viewModel.getName(),
        viewModel.getAccountStatus(),
        viewModel.getCreatedAt()
    );
  }
}
