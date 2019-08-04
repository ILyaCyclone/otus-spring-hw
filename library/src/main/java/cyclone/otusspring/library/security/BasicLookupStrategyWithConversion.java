package cyclone.otusspring.library.security;

import org.springframework.core.convert.ConversionService;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;

import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * according to // https://github.com/spring-projects/spring-security/issues/4814
 * BasicLookingStrategy#setConversionService should be available in version 5.2
 */
public class BasicLookupStrategyWithConversion extends BasicLookupStrategy {
    public BasicLookupStrategyWithConversion(DataSource dataSource, AclCache aclCache, AclAuthorizationStrategy aclAuthorizationStrategy, AuditLogger auditLogger
            , ConversionService conversionService
    ) {
        super(dataSource, aclCache, aclAuthorizationStrategy, auditLogger);
        this.setAclClassIdSupported(true);
        this.setConversionService(conversionService);
    }

    public BasicLookupStrategyWithConversion(DataSource dataSource, AclCache aclCache, AclAuthorizationStrategy aclAuthorizationStrategy, PermissionGrantingStrategy grantingStrategy
            , ConversionService conversionService) {
        super(dataSource, aclCache, aclAuthorizationStrategy, grantingStrategy);
        this.setAclClassIdSupported(true);
        this.setConversionService(conversionService);
    }



    private void setConversionService(ConversionService conversionService) {
        //TODO BasicLookingStrategy#setConversionService should be available in version 5.2
        try {
            Field aclClassIdUtilsField = this.getClass().getSuperclass().getDeclaredField("aclClassIdUtils");
            aclClassIdUtilsField.setAccessible(true);
            Object aclClassIdUtilsObject = aclClassIdUtilsField.get(this);
            Field conversionServiceField = aclClassIdUtilsObject.getClass().getDeclaredField("conversionService");
            conversionServiceField.setAccessible(true);
            conversionServiceField.set(aclClassIdUtilsObject, conversionService);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
