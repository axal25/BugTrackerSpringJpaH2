package axal25.oles.jacek.dao.application;

import axal25.oles.jacek.entity.ApplicationEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Repository
public class ApplicationDao implements IApplicationDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<ApplicationEntity> getAllApplications() {
        String jpqlQuery = "SELECT application from " +
                ApplicationEntity.class.getSimpleName() +
                " application ORDER BY application.id";
        List<?> resultList = entityManager.createQuery(jpqlQuery).getResultList();
        return resultList.stream()
                .map(application -> (ApplicationEntity) application)
                .collect(Collectors.toList());
    }

    @Override
    public void addApplication(ApplicationEntity application) {
        entityManager.persist(application);
    }

    @Override
    public boolean applicationExists(String name, String owner) {
        String jpqlQuery = "FROM " +
                ApplicationEntity.class.getSimpleName() +
                " as app WHERE app.name = ?0 and app.owner = ?1";
        int count = entityManager.createQuery(jpqlQuery)
                .setParameter(0, name)
                .setParameter(1, owner)
                .getResultList()
                .size();
        return count > 0;
    }

    @Override
    public ApplicationEntity getApplicationById(int applicationId) {
        return entityManager.find(ApplicationEntity.class, applicationId);
    }

    @Override
    public void updateApplication(ApplicationEntity updated) {
        ApplicationEntity existing = getApplicationById(updated.getId());
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setOwner(updated.getOwner());
        entityManager.flush();
    }

    @Override
    public void deleteApplication(int applicationId) {
        entityManager.remove(getApplicationById(applicationId));
    }
}
