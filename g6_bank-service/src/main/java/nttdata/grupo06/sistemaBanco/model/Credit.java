package nttdata.grupo06.sistemaBanco.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "Assets")
@Data
@NoArgsConstructor
public class Credit {

    private String id;
    private String creditType;
    private String status;
    private Float initialAmount;
    private Float currentAmount;
    private Float interest;
    private Integer paymentDay;
    private String creditAccountNumber;
    private String clientId;
    private String cardNumber;
}


