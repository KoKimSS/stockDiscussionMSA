package com.example.stockmsauser.domain.certification;

import com.example.stockmsauser.domain.baseEntity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certification extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "certification_id")
    Long id;

    String email;
    String certificationNumber;
    Boolean isCertified ;

    @Builder
    private Certification(String email,String certificationNumber,LocalDateTime localDateTime){
        this.email = email;
        this.certificationNumber = certificationNumber;
        this.isCertified = false;
    }

    public void certificated(){
        this.isCertified = true;
    }
}
