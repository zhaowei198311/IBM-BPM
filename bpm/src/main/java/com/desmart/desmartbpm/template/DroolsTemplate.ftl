package com.demart.desmartbpm.drools
import java.util.Map; 
import java.util.HashMap; 
rule "${ruleName}"
date-effective '${effective}'
date-expires '${expires}'
when
$m : Map()
${ruleProcess}
then
$m.clear();
$m.put('state',true);
end