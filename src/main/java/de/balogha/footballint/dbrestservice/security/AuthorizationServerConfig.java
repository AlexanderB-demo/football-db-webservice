package de.balogha.footballint.dbrestservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private static String REALM="FOOTBALLDB_OAUTH_REALM";

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${footballint.dbrestservice.oauth2.readOnlyClientSecret}")
    private String readOnlyClientSecret;

    @Value("${footballint.dbrestservice.oauth2.trustedClientSecret}")
    private String trustedClientSecret;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                    .withClient("read-only-client")
                    .secret(readOnlyClientSecret)
                    .authorizedGrantTypes("client_credentials")
                    .scopes("read")
                    .authorities("ROLE_READ_ONLY_CLIENT")
                    .accessTokenValiditySeconds(1200)
                .and()
                    .withClient("trusted-client")
                    .secret(trustedClientSecret)
                    .authorizedGrantTypes("client_credentials")
                    .scopes("read", "write", "trust")
                    .authorities("ROLE_READ_ONLY_CLIENT", "ROLE_TRUSTED_CLIENT")
                    .accessTokenValiditySeconds(1200);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .userApprovalHandler(userApprovalHandler)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.realm(REALM + "/client");
    }
}
