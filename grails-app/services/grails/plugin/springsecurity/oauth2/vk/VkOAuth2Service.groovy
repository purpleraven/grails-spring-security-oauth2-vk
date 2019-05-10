package grails.plugin.springsecurity.oauth2.vk

import com.github.scribejava.apis.VkontakteApi
import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.model.OAuth2AccessToken
import grails.converters.JSON
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugin.springsecurity.oauth2.service.OAuth2AbstractProviderService
import grails.plugin.springsecurity.oauth2.token.OAuth2SpringToken
import grails.transaction.Transactional

@Transactional
class VkOAuth2Service extends OAuth2AbstractProviderService {

    @Override
    String getProviderID() {
        return 'vk'
    }

    @Override
    Class<? extends DefaultApi20> getApiClass() {
        VkontakteApi.class
    }

    @Override
    String getProfileScope() {
        return 'https://api.vk.com/method/users.get'
    }

    @Override
    String getScopes() {
        return 'email,status'
    }

    @Override
    String getScopeSeparator() {
        return ","
    }

    @Override
    OAuth2SpringToken createSpringAuthToken(OAuth2AccessToken accessToken) {
        def user
        def token
        def response = getResponse(accessToken)
        try {
            log.warn("JSON response body: " + accessToken.rawResponse)
            user = JSON.parse(response.body)
            token = JSON.parse(accessToken.rawResponse)

        } catch (Exception exception) {
            log.error("Error parsing response from " + getProviderID() + ". Response:\n" + response.body)
            throw new OAuth2Exception("Error parsing response from " + getProviderID(), exception)
        }
//        if (!user?.email) {
//            log.error("No user email from " + getProviderID() + ". Response was:\n" + response.body)
//            throw new OAuth2Exception("No user id from " + getProviderID())
//        }
        String id = token?.email ?: user.uid
        if (!id){
            log.warn "No email in ${user}, JSON response body: ${accessToken.rawResponse}, ${token}"
        }
        new VkOauth2SpringToken(accessToken, id, providerID)
    }
}
