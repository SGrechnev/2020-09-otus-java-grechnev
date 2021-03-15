package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.db.handlers.GetAllUserRequestHandler;
import ru.otus.db.handlers.GetRandomUserRequestHandler;
import ru.otus.db.handlers.GetUserRequestHandler;
import ru.otus.db.handlers.SaveUserRequestHandler;
import ru.otus.db.service.UserService;
import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.front.handlers.GetUserDataResponseHandler;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

@Configuration
public class MessageSystemConfig {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemConfig.class);

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    // MS::Front
    @Bean
    public FrontendService frontendService(MessageSystem messageSystem, MsClient frontendMsClient) {
        return new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
    }

    @Bean(name = "frontendMsClient")
    public MsClient frontendMsClient(MessageSystem messageSystem, HandlersStore requestHandlerFrontendStore,
                                     CallbackRegistry callbackRegistry) {
        MsClient databaseMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem,
                requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean(name = "requestHandlerFrontendStore")
    public HandlersStore requestHandlerFrontendStore(GetUserDataResponseHandler getUserDataResponseHandler) {
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.USER_DATA, getUserDataResponseHandler);
        return requestHandlerFrontendStore;
    }

    @Bean
    public GetUserDataResponseHandler getUserDataResponseHandler(CallbackRegistry callbackRegistry) {
        return new GetUserDataResponseHandler(callbackRegistry);
    }

    // MS::DB
    @Bean(name = "databaseMsClient")
    public MsClient databaseMsClient(MessageSystem messageSystem, HandlersStore requestHandlerDatabaseStore,
                                     CallbackRegistry callbackRegistry) {
        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem,
                requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean(name = "requestHandlerDatabaseStore")
    public HandlersStore requestHandlerDatabaseStore(GetUserRequestHandler getUserRequestHandler,
                                                     GetAllUserRequestHandler getAllUserRequestHandler,
                                                     GetRandomUserRequestHandler getRandomUserRequestHandler,
                                                     SaveUserRequestHandler saveUserRequestHandler) {
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.GET_USER, getUserRequestHandler);
        requestHandlerDatabaseStore.addHandler(MessageType.GET_ALL, getAllUserRequestHandler);
        requestHandlerDatabaseStore.addHandler(MessageType.GET_RANDOM, getRandomUserRequestHandler);
        requestHandlerDatabaseStore.addHandler(MessageType.SAVE_USER, saveUserRequestHandler);
        return requestHandlerDatabaseStore;
    }

    @Bean
    public GetUserRequestHandler getUserDataRequestHandler(UserService userService) {
        return new GetUserRequestHandler(userService);
    }

    @Bean
    public GetAllUserRequestHandler getAllUserRequestHandler(UserService userService) {
        return new GetAllUserRequestHandler(userService);
    }

    @Bean
    public GetRandomUserRequestHandler getRandomUserRequestHandler(UserService userService) {
        return new GetRandomUserRequestHandler(userService);
    }

    @Bean
    public SaveUserRequestHandler saveUserRequestHandler(UserService userService) {
        return new SaveUserRequestHandler(userService);
    }
}
