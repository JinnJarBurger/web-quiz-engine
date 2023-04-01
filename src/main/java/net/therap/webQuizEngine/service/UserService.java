package net.therap.webQuizEngine.service;

import net.therap.webQuizEngine.exception.NotAuthorizedException;
import net.therap.webQuizEngine.exception.NotFoundException;
import net.therap.webQuizEngine.helper.UserHelper;
import net.therap.webQuizEngine.model.User;
import net.therap.webQuizEngine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import static net.therap.webQuizEngine.model.Role.ADMIN;

/**
 * @author adnan
 * @since 7/18/2022
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private MessageSourceAccessor msa;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(msa.getMessage("error.no.such.user")));
    }

    public User saveOrUpdate(User user) {
        if (user.isNew()) {
            if (user.getRole().equals(ADMIN)) {
                throw new NotAuthorizedException(msa.getMessage("error.action.not.authorized"));
            }

            userHelper.calculateAge(user);
        }

        return userRepository.save(user);
    }
}
