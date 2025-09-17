package co.credit.app.api.mapper;

import co.credit.app.api.dto.LoanDTO;
import co.credit.app.model.loan.Loan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanDTOMapper {

  LoanDTO toResponse(Loan loan);

}
