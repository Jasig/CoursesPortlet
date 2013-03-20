
<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div class="box_credits_gpa">
	<div class="left box_credits">
		<div class="left box_credits_text">Term Credits</div>
		<div class="left box_credits_numbers">${ courseSummary.credits }</div>

		<div class="clear_left left box_credits_text font_weight_bold">Cum. Credits</div>
		<div class="left box_credits_numbers font_weight_bold">${
			courseSummary.overallCredits }</div>
	</div>

	<div class="left box_gpa">
		<div class="left box_gpa_text ">Term GPA</div>
		<div class="left box_gpa_numbers">${ courseSummary.gpa }</div>

		<div class="clear_left left box_gpa_text font_weight_bold">Cum. GPA</div>
		<div class="left box_gpa_numbers font_weight_bold">${ courseSummary.overallGpa }</div>
	</div>
</div>