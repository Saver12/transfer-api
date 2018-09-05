package com.saver12.config;

import com.saver12.service.AccountService;
import com.saver12.service.TransferService;
import com.saver12.service.impl.AccountServiceImpl;
import com.saver12.service.UserService;
import com.saver12.service.impl.TransferServiceImpl;
import com.saver12.service.impl.UserServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class ApplicationBinder extends AbstractBinder {
	
    @Override
    protected void configure() {
        bind(UserServiceImpl.class).to(UserService.class).in(Singleton.class);
        bind(AccountServiceImpl.class).to(AccountService.class).in(Singleton.class);
        bind(TransferServiceImpl.class).to(TransferService.class).in(Singleton.class);
    }
}
