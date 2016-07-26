package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.io.Serializable;


@Element(name="method")
public class Method implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;

    @Attribute
    private Target target;

    public Method()
    {}

    public Method(String name, Target target)
    {
        this.name = name;
        this.target = target;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Target getTarget()
    {
        return target;
    }

    public void setTarget(Target target)
    {
        this.target = target;
    }

    @Override
    public String toString()
    {
        return name + "/" + target;
    }
}
