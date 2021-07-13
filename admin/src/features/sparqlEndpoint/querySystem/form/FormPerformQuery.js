import React from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from '../../../sourcesManagement/addSource/forms/asyncValidate'
import {Button, Grid} from "@material-ui/core";
import { renderTextField, useStyles} from "../../../sourcesManagement/addSource/forms/FormElements";
import {Col} from "react-bootstrap";


const validate = values => {
    const errors = {}
    const requiredFields = ["query"]
    requiredFields.forEach(field => {
        if (!values[field]) {
            errors[field] = 'Required'
        }
    })
    return errors
}

const PerformQueryForm = props => {
    const { handleSubmit, classes } = props
    const c = useStyles();

    return (
        <div style={{width: "97%", paddingTop: "3%"}}>
            <form className={c.root} style={{width: "100%"}} onSubmit={handleSubmit} name="performQueryForm">
                <Grid container spacing={2}>
                    <Grid item xs={12} style={{marginTop: "-3.5%"}}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="query"
                            component={renderTextField}
                            label="SPARQL Query"
                            className={c.field}
                            labelText="Insert SPARQL Query. You can use the prefixes present on the 'List Prefixes' card."
                            multiline
                            rows={8}
                        />
                    </Grid>
                </Grid>

                <Col sm={12} className="text-right" style={{paddingRight: "0", paddingBottom: "30px"}}>
                    <Button variant="outlined" className={ c.buttonG } type="submit" disabled={ props.pristine || props.submitting || props.invalid }>
                        Query
                    </Button>
                </Col>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'PerformQueryForm', // a unique identifier for this form
    validate
})(PerformQueryForm)
