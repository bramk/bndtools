package org.bndtools.core.resolve;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import aQute.bnd.build.model.BndEditModel;
import aQute.bnd.build.model.EE;
import biz.aQute.resolve.ResolutionCallback;
import bndtools.Plugin;

public class ResolveJob extends Job {

    private final BndEditModel model;
    private final List<ResolutionCallback> callbacks = new LinkedList<ResolutionCallback>();

    private ResolutionResult result;

    public ResolveJob(BndEditModel model) {
        super("Resolving...");
        this.model = model;
    }

    public IStatus validateBeforeRun() {
        String runfw = model.getRunFw();
        if (runfw == null)
            return new Status(IStatus.ERROR, Plugin.PLUGIN_ID, 0, Messages.ResolutionJob_errorFrameworkOrExecutionEnvironmentUnspecified, null);

        EE ee = model.getEE();
        if (ee == null)
            return new Status(IStatus.ERROR, Plugin.PLUGIN_ID, 0, Messages.ResolutionJob_errorFrameworkOrExecutionEnvironmentUnspecified, null);

        return Status.OK_STATUS;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        ResolveOperation operation = new ResolveOperation(model, callbacks);
        operation.run(monitor);
        result = operation.getResult();

        return Status.OK_STATUS;
    }

    public ResolutionResult getResolutionResult() {
        return result;
    }

    public void addCallback(ResolutionCallback callback) {
        callbacks.add(callback);
    }

}
