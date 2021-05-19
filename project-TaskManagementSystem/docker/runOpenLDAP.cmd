docker run --rm --name openldap ^
  --env LDAP_ADMIN_USERNAME=admin ^
  --env LDAP_ADMIN_PASSWORD=adminpassword ^
  --env LDAP_USERS=admin,manager1,manager2,performer1,performer2,performer3,performer4,performer5 ^
  --env LDAP_PASSWORDS=apassword,m1password,m2password,p1password,p2password,p3password,p4password,p5password ^
  -p 127.0.0.1:9389:1389 ^
  bitnami/openldap:latest