package com.smallbusiness.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    private String phoneNumber;

    private String mobilePhone;

    @Column(columnDefinition = "TEXT")
    private String position;

    @Column(columnDefinition = "TEXT")
    private String department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactSource source;

    private String linkedInProfile;

    private String skypeId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public enum ContactStatus {
        ACTIVE, INACTIVE, DO_NOT_CONTACT
    }

    public enum ContactSource {
        PHONE_INQUIRY, EMAIL, WEBSITE, SOCIAL_MEDIA, REFERRAL, ADVERTISEMENT, TRADE_SHOW, OTHER
    }
}
