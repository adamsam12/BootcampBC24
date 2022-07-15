package nttdata.grupo06.sistemaBanco.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collation = "Clients")
public class Clients {
    @Id
    private String clientId;
    private String identityDocument;
    private String name;
    private String lastnames;
    private int phoneNumber;
    private String mail;
    private Integer clientType;
    private String clientSubType;
    private String address;
}
