package cyclone.otusspring.library.service;

import cyclone.otusspring.library.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LibraryAclService {

    private final MutableAclService aclService;

    @Transactional
    public void grantNewBookPermissions(String bookId) {
        grantPermissionToBook(bookId, BasePermission.READ, new GrantedAuthoritySid("ROLE_USER"));
        grantPermissionToBook(bookId, BasePermission.ADMINISTRATION, new GrantedAuthoritySid("ROLE_ADMIN"));
    }

    @Transactional
    public void grantPermissionToBook(String bookId, Permission permission, Sid sid) {
        ObjectIdentityImpl aclObjectIdentity = new ObjectIdentityImpl(Book.class, bookId);

        MutableAcl acl = null;
        try {
            acl = (MutableAcl) aclService.readAclById(aclObjectIdentity);
        } catch (NotFoundException e) {
            acl = aclService.createAcl(aclObjectIdentity);
        }

        acl.insertAce(acl.getEntries().size(), permission, sid, true);
        aclService.updateAcl(acl);
    }
}
