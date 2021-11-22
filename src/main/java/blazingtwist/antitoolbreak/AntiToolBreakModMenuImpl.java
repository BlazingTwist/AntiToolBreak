package blazingtwist.antitoolbreak;

import blazingtwist.antitoolbreak.config.AntiToolBreakConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public class AntiToolBreakModMenuImpl implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(AntiToolBreakConfig.class, parent).get();
	}
}
