package org.logSpin.plugin;

import org.logSpin.Exception.NoPluginFoundException;
import org.logSpin.Plugin;
import org.logSpin.PluginContainer;
import org.logSpin.PluginManager;
import org.logSpin.Spin;

public class DefaultPluginManager implements PluginManager<Spin> {
    private final PluginRegister pluginRegister = new PluginRegister();
    private final PluginContainer<Plugin<Spin>> pluginContainer;

    public DefaultPluginManager(PluginContainer<Plugin<Spin>> pluginContainer) {
        this.pluginContainer = pluginContainer;
    }

    public PluginRegister getPluginRegister() {
        return pluginRegister;
    }

    @Override
    public Plugin<Spin> getPluginById(String id) {
        try {
            if (contain(id)) {
                return pluginContainer.getPlugins().get(id);
            } else {
                PluginId pluginId = pluginRegister.getPlugin(id);
                if (pluginId != null) {
                    return pluginRegister.getPlugin(id).getPluginClass().newInstance();
                }else{
                    throw new NoPluginFoundException(id);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PluginContainer<Plugin<Spin>> getPluginContainer() {
        return pluginContainer;
    }

    @Override
    public boolean contain(String id) {
        return pluginContainer.contain(id);
    }

    @Override
    public void addPlugin(String id) {
        pluginContainer.addPlugin(id, getPluginById(id));
    }
}
