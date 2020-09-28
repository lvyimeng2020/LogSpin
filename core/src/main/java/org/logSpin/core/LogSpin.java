package org.logSpin.core;

import org.logSpin.*;
import org.logSpin.plugin.DefaultPluginManager;
import org.logSpin.script.ScriptRunner;

import java.util.ArrayList;
import java.util.List;

public class LogSpin implements Spin {

    private final ScriptRunner scriptRunner;
    private final PluginManager pluginManager;
    private final List<SpinCase> resolvedCase;
    private final List<SpinCase> configuredCase;
    private final LogProcess logProcess;

    public LogSpin(PluginContainer<Plugin<?>> pluginContainer, ScriptRunner scriptRunner, LogProcess logProcess) {
        this.scriptRunner = scriptRunner;
        this.pluginManager = new DefaultPluginManager(pluginContainer);
        this.logProcess = logProcess;
        this.resolvedCase = new ArrayList<>();
        this.configuredCase = new ArrayList<>();
    }

    @Override
    public void configure(String[] arg) {
        scriptRunner.runScriptToConfigSpin(arg);
        resolveCase();
    }


    private void resolveCase() {
        getPluginContainer().getPlugins().forEach(
                plugin ->
                        plugin.resolveCase(this)

        );
        analyse();
    }

    @Override
    public void analyse() {
        getResolvedCases().forEach(spinCase ->
                spinCase.action(getLogProcess())
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public PluginContainer<Plugin<Spin>> getPluginContainer() {
        return (PluginContainer<Plugin<Spin>>) pluginManager.getPluginContainer();
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public LogProcess getLogProcess() {
        return logProcess;
    }

    @Override
    public List<SpinCase> getResolvedCases() {
        return resolvedCase;
    }

    @Override
    public List<SpinCase> getConfiguredCase() {
        return configuredCase;
    }
}
