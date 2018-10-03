package com.idc.idc.model.users;

import com.idc.idc.model.abstracts.AbstractUserEntity;
import com.idc.idc.model.enums.UserState;
import lombok.*;

import javax.persistence.*;

@Table(name = "customers", indexes = {
        @Index(name = "email_index", columnList = Customer.EMAIL)
})
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AbstractUserEntity {
    @Column
    @Enumerated(EnumType.STRING)
    private UserState state;
}



