/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.stdlib.system.nativeimpl;

import org.ballerinalang.jvm.values.ArrayValue;
import org.ballerinalang.jvm.values.MapValue;
import org.ballerinalang.stdlib.system.utils.SystemConstants;
import org.ballerinalang.stdlib.system.utils.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Extern function ballerina.system:exec.
 *
 * @since 1.0.0
 */
public class Exec {

    private static final Logger log = LoggerFactory.getLogger(Exec.class);

    public static Object exec(String command, MapValue<String, String> env, Object dir,
                              ArrayValue args) {
        List<String> commandList = new ArrayList<String>();
        commandList.add(command);
        if (args.size() > 0) {
            commandList.addAll(Arrays.asList(args.getStringArray()));
        }
        ProcessBuilder pb = new ProcessBuilder(commandList);
        if (dir != null) {
            pb.directory(new File((String) dir));
        }
        if (env != null) {
            Map<String, String> pbEnv = pb.environment();
            env.entrySet().forEach(entry -> pbEnv.put(entry.getKey(), entry.getValue()));
        }
        try {
            return SystemUtils.getProcessObject(pb.start());
        } catch (IOException e) {
            log.error("IO error while executing the command: " + commandList, e);
            return SystemUtils.getBallerinaError(SystemConstants.PROCESS_EXEC_ERROR, e);
        }
    }

}
