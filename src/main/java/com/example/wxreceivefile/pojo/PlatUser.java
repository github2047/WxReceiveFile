package com.example.wxreceivefile.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlatUser {
  private long id;
  private String loginName;
  private String userName;
  private String sex;
  private String role;
  private String email;
  private String phone;
  private String isUse;
  private String department;
  private String language;
  private String chargStandard;
  private String quota;
  private String documentLevel;
  private String assessor;
  private String password;
  private String sid;
  private String dn;
  private String isReport;
  private String unionid;
  private long confId;
  private String wxConfId;
  private long isDeleted;
  private String domainConfId;
  private String otherConfType;
  private String otherUserId;
  private String otherConfId;
}
