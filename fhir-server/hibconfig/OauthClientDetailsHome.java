// default package
// Generated Dec 29, 2016 12:38:13 PM by Hibernate Tools 4.0.0.Final

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class OauthClientDetails.
 * @see .OauthClientDetails
 * @author Hibernate Tools
 */
@Stateless
public class OauthClientDetailsHome {

	private static final Log log = LogFactory.getLog(OauthClientDetailsHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(OauthClientDetails transientInstance) {
		log.debug("persisting OauthClientDetails instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(OauthClientDetails persistentInstance) {
		log.debug("removing OauthClientDetails instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public OauthClientDetails merge(OauthClientDetails detachedInstance) {
		log.debug("merging OauthClientDetails instance");
		try {
			OauthClientDetails result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public OauthClientDetails findById(String id) {
		log.debug("getting OauthClientDetails instance with id: " + id);
		try {
			OauthClientDetails instance = entityManager.find(OauthClientDetails.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
