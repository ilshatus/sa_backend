package com.idc.idc.model.users;

import com.idc.idc.model.Vehicle;
import com.idc.idc.model.abstracts.AbstractUserEntity;
import com.idc.idc.model.embeddable.CurrentLocation;
import lombok.*;

import javax.persistence.*;

@Table(name = "drivers", indexes = {
        @Index(name = "email_index", columnList = Customer.EMAIL)
})
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends AbstractUserEntity {
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String VEHICLE_ID = "vehicle_id";
    public static final String FIREBASE_TOKEN = "firebase_token";
    public static final String AVATAR_URL = "avatarUrl";

    @Embedded
    private CurrentLocation location;

    @Column(name = EMAIL, length = 100, unique = true)
    private String email;

    @Column(name = NAME, length = 250)
    private String name;

    @ManyToOne
    @JoinColumn(name = VEHICLE_ID)
    private Vehicle vehicle;

    @Column(name = FIREBASE_TOKEN)
    private String firebaseToken;

    @Column(name = AVATAR_URL)
    private String avatarUrl;
}
