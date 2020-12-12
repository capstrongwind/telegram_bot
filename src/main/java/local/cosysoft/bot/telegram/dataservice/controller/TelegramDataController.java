package local.cosysoft.bot.telegram.dataservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram/data")
public class TelegramDataController {
    private final TelegramDataService telegramDataService;

    @GetMapping("/poll")
    public void getPoll() {

    }
}
