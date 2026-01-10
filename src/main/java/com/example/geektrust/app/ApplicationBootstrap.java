package com.example.geektrust.app;

import com.example.geektrust.cli.LineParser;
import com.example.geektrust.dispatch.CommandHandlers;
import com.example.geektrust.dispatch.Dispatcher;
import com.example.geektrust.domain.Portfolio;
import com.example.geektrust.service.PortfolioService;

public class ApplicationBootstrap {

  private ApplicationBootstrap() {}

  public static AppRunner createRunner() {
    Portfolio portfolio = new Portfolio();
    PortfolioService portfolioService = new PortfolioService(portfolio);
    MyMoneyAppService app = new MyMoneyAppService(portfolioService);

    Dispatcher dispatcher = new Dispatcher(CommandHandlers.build(app));
    return new AppRunner(dispatcher, new LineParser());
  }

}
