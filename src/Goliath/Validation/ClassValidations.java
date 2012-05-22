/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Validation;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Arguments.Arguments;

/**
 *
 * @author kmchugh
 */
public class ClassValidations extends Goliath.Object
{
    private static HashTable<Class<RuleHandler>, RuleHandler> g_oCachedHandlers;
    private static HashTable<Class, HashTable<String, HashTable<Class<RuleHandler>, Arguments>>> g_oRules;

    /**
     * Adds rules to the global validation object for the specified class.  If the
     * rule already exists for the class and property, it will not be added again
     * @param toClass the class to add the rules for
     * @param toRule the rule to add for validation
     * @param tcPropertyName the property to validate against
     */
    public static <T extends RuleHandler> void addRule(Class toClass, Class<T> toRuleClass, String tcPropertyName, Arguments toArgs)
    {


        if (g_oRules == null)
        {
            g_oRules = new HashTable<Class, HashTable<String, HashTable<Class<RuleHandler>, Arguments>>>();
        }

        if (!g_oRules.containsKey(toClass))
        {
            g_oRules.put(toClass, new HashTable<String, HashTable<Class<RuleHandler>, Arguments>>());
        }

        if (!g_oRules.get(toClass).containsKey(tcPropertyName))
        {
            g_oRules.get(toClass).put(tcPropertyName, new HashTable<Class<RuleHandler>, Arguments>());
        }

        if (!g_oRules.get(toClass).get(tcPropertyName).contains(toRuleClass))
        {
            try
            {
                g_oRules.get(toClass).get(tcPropertyName).put((Class<RuleHandler>)toRuleClass, (toArgs == null) ? new Arguments() : toArgs);
            }
            catch(Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }
    }

    /**
     * Takes a broken rules collection and returns a collection of broken rules that apply to the specified property
     * This method does not change the original collection
     * @param toCollection the original collection
     * @param tcProperty the property to get the rules from
     * @return A list of broken rules against the specified property
     */
    public static BrokenRulesCollection filterBrokenRulesByProperty(BrokenRulesCollection toCollection, String tcProperty)
    {
        BrokenRulesCollection loReturn = new BrokenRulesCollection();

        for (BrokenRule loRule : toCollection)
        {
            if (loRule.getProperty().equalsIgnoreCase(tcProperty))
            {
                loReturn.add(loRule);
            }
        }
        return loReturn;
    }

    /**
     * Validates the object passed in.  This will return a list of any rules that have been broken,
     * or an empty broken rules collection if no rules were checked or failed
     * @param toObject the object to validate
     * @return the list of rules that the object failed on, or an empty list if the object did not fail any rules
     */
    public static BrokenRulesCollection validate(Object toObject)
    {
        BrokenRulesCollection loReturn = new BrokenRulesCollection();

        // Make sure the object is not null, and that we have rules for this type of object
        if (toObject != null && g_oRules.containsKey(toObject.getClass()))
        {
            for(String lcProperty : g_oRules.get(toObject.getClass()).keySet())
            {
                loReturn.addAll(validate(toObject, lcProperty));
            }
        }
        return loReturn;
    }

    /**
     * Validates only the rules applying to the specified property
     * @param toObject the object to validate
     * @param tcProperty the property to validate
     * @return
     */
    public static BrokenRulesCollection validate(Object toObject, String tcProperty)
    {
        BrokenRulesCollection loReturn = new BrokenRulesCollection();

        // Make sure the object is not null, and that we have rules for this type of object
        if (toObject != null && g_oRules.containsKey(toObject.getClass()) && g_oRules.get(toObject.getClass()).containsKey(tcProperty))
        {
             // Get the property value so that we are not constantly getting it in the loop
            Object loValue = Goliath.DynamicCode.Java.getPropertyValue(toObject, tcProperty);


            // Get the list of all the rule handlers for this property
            HashTable<Class<RuleHandler>, Arguments> loHandlers = g_oRules.get(toObject.getClass()).get(tcProperty);
            for (Class<RuleHandler> loKey : loHandlers.keySet())
            {
                RuleHandler loHandler = getHandler(loKey);
                if (loHandler != null)
                {
                    // Execute the rule handler with the specified object and value
                    if (!loHandler.validate(toObject, loValue, loHandlers.get(loKey)))
                    {
                        loReturn.add(new BrokenRule(loHandler, toObject, tcProperty, loValue, loHandlers.get(loKey)));
                    }
                }
            }
        }
        return loReturn;
    }

    /**
     * Helper function to enforce a flyweight on the actual rule handler classes
     * Pass in the class of the rule and get back an instantiation
     * @param toClass the class of the rule handler to get
     * @return the instantiation of the rule
     */
    private static RuleHandler getHandler(Class<RuleHandler> toClass)
    {
        if (g_oCachedHandlers == null)
        {
            g_oCachedHandlers = new HashTable<Class<RuleHandler>, RuleHandler>();
        }

        if (!g_oCachedHandlers.contains(toClass))
        {
            try
            {
                g_oCachedHandlers.put(toClass, toClass.newInstance());
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }
        return g_oCachedHandlers.get(toClass);
    }


}
