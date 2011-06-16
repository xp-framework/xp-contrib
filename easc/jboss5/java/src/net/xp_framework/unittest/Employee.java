/* This class is part of the XP framework's EAS connectivity
 *
 * $Id: Employee.java 6601 2006-02-14 13:30:40Z friebe $
 */

package net.xp_framework.unittest;

/**
 * Person class
 *
 * @purpose Value object for SerializerTest
 */
public class Employee extends Person {
    public int personellNumber;

    private Employee() { }

    public Employee(int personellNumber) { 
        this.personellNumber= personellNumber;
    }
}
