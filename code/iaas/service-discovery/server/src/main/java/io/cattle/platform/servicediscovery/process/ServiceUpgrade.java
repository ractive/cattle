package io.cattle.platform.servicediscovery.process;

import io.cattle.platform.core.model.Service;
import io.cattle.platform.engine.handler.HandlerResult;
import io.cattle.platform.engine.process.ProcessInstance;
import io.cattle.platform.engine.process.ProcessState;
import io.cattle.platform.json.JsonMapper;
import io.cattle.platform.process.base.AbstractDefaultProcessHandler;
import io.cattle.platform.servicediscovery.api.constants.ServiceDiscoveryConstants;
import io.cattle.platform.servicediscovery.upgrade.UpgradeManager;

import javax.inject.Inject;

public class ServiceUpgrade extends AbstractDefaultProcessHandler {

    @Inject
    JsonMapper jsonMapper;

    @Inject
    UpgradeManager upgradeManager;

    @Override
    public HandlerResult handle(ProcessState state, ProcessInstance process) {
        io.cattle.platform.core.addon.ServiceUpgrade upgrade = jsonMapper.convertValue(state.getData(),
                io.cattle.platform.core.addon.ServiceUpgrade.class);
        Service service = (Service)state.getResource();

        objectManager.setFields(service, ServiceDiscoveryConstants.FIELD_UPGRADE, upgrade);

        upgradeManager.upgrade(service, upgrade.getStrategy());

        return null;
    }
}
