package com.its.vdv.rest.wrapper;

import com.crashlytics.android.Crashlytics;
import com.its.vdv.rest.configuration.RestConfiguration;
import com.its.vdv.rest.raw.ImageRest;
import com.its.vdv.rest.raw.UserRest;
import com.its.vdv.rest.request.CreateUserRequest;
import com.its.vdv.service.AuthService;
import com.its.vdv.service.UserService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import static com.its.vdv.rest.configuration.RestConfiguration.BACKEND_ROOT;

@EBean
public class UserRestWrapper {
    @Bean
    UserService userService;
    @Bean
    AuthService authService;

    @RestService
    ImageRest imageRest;
    @RestService
    UserRest userRest;

    @Background
    public void createUser(String email, String name, RestListener<Void> listener) {
        try {
            listener.onStart();

            CreateUserRequest request = new CreateUserRequest();
            {
                request.setUsername(name);
                request.setE_mail(email);
            }

            userRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            userRest.createUser(request);

            listener.onSuccess(null);
        } catch (Exception e) {
            /* Do nothing */

            listener.onSuccess(null);
        }
    }

    public byte [] getUserImage(long id, String path, RestListener<byte []> listener) {
        try {
            listener.onStart();

            byte [] image = imageRest.getImage(path);

            userService.setUserImage(id, image);

            listener.onSuccess(image);

            return image;
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);

            return null;
        }
    }
}
