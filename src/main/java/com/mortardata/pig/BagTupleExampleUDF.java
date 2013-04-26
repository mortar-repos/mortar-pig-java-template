package com.mortardata.pig;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import java.io.IOException;
import java.util.Collections;

/**
 * Example UDF that takes two integers and returns a bag with two tuples: one containing the
 * product and one the quotient.  Written to show how to use bags and tuples, not because
 * it's useful in any way.
 */
public class BagTupleExampleUDF extends EvalFunc<DataBag> {

    TupleFactory mTupleFactory = TupleFactory.getInstance();
    BagFactory mBagFactory = BagFactory.getInstance();


    @Override
    public DataBag exec(Tuple tuple) throws IOException {
        // expect one string
        if (tuple == null || tuple.size() != 2) {
            throw new IllegalArgumentException("BagTupleExampleUDF: requires two input parameters.");
        }
        try {
            Double first = DataType.toDouble(tuple.get(0));
            Double second  = DataType.toDouble(tuple.get(1));

            DataBag output = mBagFactory.newDefaultBag();
            output.add(mTupleFactory.newTuple(Collections.singletonList(first * second)));
            output.add(mTupleFactory.newTuple(Collections.singletonList(first / second)));
            return output;
        }
        catch (Exception e) {
            throw new IOException("BagTupleExampleUDF: caught exception processing input.", e);
        }
    }

    public Schema outputSchema(Schema input) {
        try{
            Schema bagSchema = new Schema();
            bagSchema.add(new Schema.FieldSchema("number", DataType.DOUBLE));

            return new Schema(new Schema.FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input),
                    bagSchema, DataType.BAG));
        }catch (Exception e){
            return null;
        }
    }
}
