/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.degreeprogress.model.xml;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

abstract public class DegreeRequirementSectionWrapper {
    protected Double requiredCredits;
    protected Double requiredGpa;
    protected Map<String, CourseRequirement> courseRequirements = new LinkedHashMap<String, CourseRequirement>();


    public Double getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(Double requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public Double getRequiredGpa() {
        return requiredGpa;
    }

    public void setRequiredGpa(Double requiredGpa) {
        this.requiredGpa = requiredGpa;
    }

    public Collection<CourseRequirement> getCourseRequirements() {
        return Collections.unmodifiableCollection(courseRequirements.values());
    }

    public CourseRequirement getCourseRequirement(String key) {
        return this.courseRequirements.get(key);
    }

    public void addCourseRequirement(CourseRequirement courseRequirement) {
        this.courseRequirements.put(courseRequirement.getName(), courseRequirement);
    }
}
