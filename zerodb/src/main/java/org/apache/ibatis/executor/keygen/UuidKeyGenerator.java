/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.executor.keygen;

import java.sql.Statement;
import java.util.UUID;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

/**
 * @author wangls
 */
public class UuidKeyGenerator implements KeyGenerator {

	public static final String SELECT_KEY_SUFFIX = "!uuidKey";
	private String keyProperty;

	public UuidKeyGenerator(String keyProperty) {
		this.keyProperty = keyProperty;
	}

	@Override
	public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		try {
			if (parameter != null) {
				final Configuration configuration = ms.getConfiguration();
				final MetaObject metaParam = configuration.newMetaObject(parameter);
				setValue(metaParam, keyProperty, uuid);
			}
		} catch (ExecutorException e) {
			throw e;
		} catch (Exception e) {
			throw new ExecutorException("Error selecting key or setting result to parameter object. Cause: " + e, e);
		}
	}

	@Override
	public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {

	}

	private void setValue(MetaObject metaParam, String property, Object value) {
		if (metaParam.hasSetter(property)) {
			metaParam.setValue(property, value);
		} else {
			throw new ExecutorException("No setter found for the keyProperty '" + property + "' in "
					+ metaParam.getOriginalObject().getClass().getName() + ".");
		}
	}

}
