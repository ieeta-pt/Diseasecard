import React, {useEffect} from 'react'
import { Field, reduxForm } from 'redux-form'
import {Button, Grid, MenuItem} from "@material-ui/core";
import { renderSelectField, renderTextField, useStyles} from "../../../sourcesManagement/addSource/forms/FormElements";
import {Col} from "react-bootstrap";


const validate = values => {
    const errors = {}
    const requiredFields = [
        'resourceLabel',
        'baseURL'
    ]
    requiredFields.forEach(field => {
        if (!values[field]) {
            errors[field] = 'Required'
        }
    })
    if ( values.baseURL ) {
        if (!values.baseURL.includes("#replace#")) errors.baseURL = 'URL inserted does not contains "#replace#" substring.'
    }
    return errors
}

const AddSourceBaseURLForm = props => {
    const { handleSubmit, classes, labels} = props
    const c = useStyles();

    const resourcesLabels = labels

    return (
        <div style={{width: "97.5%"}}>
            <form className={c.root} style={{width: "100%"}} onSubmit={handleSubmit} name="addSourceBaseURLForm">
                <Grid container spacing={2}>
                    <Grid item xs={3}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="resourceLabel"
                            component={renderSelectField}
                            label="Label"
                            className={c.field}
                            labelText="Choose the resource associate to the base URL you want to insert. "
                        >
                            <MenuItem value=""><em>None</em></MenuItem>
                            {
                                Object.keys(resourcesLabels).map((key,i) => {
                                    return (<MenuItem key={i} value={resourcesLabels[key]}> {resourcesLabels[key]} </MenuItem>)
                                })
                            }
                        </Field>

                    </Grid>
                    <Grid item xs={9} >
                        <Field
                            size="small"
                            variant="outlined"
                            name="baseURL"
                            component={renderTextField}
                            label="Base URL"
                            className={c.field}
                            labelText="Base URL. It must contains '#replace#'."
                        />
                    </Grid>
                </Grid>
                <Col sm={12} className="text-right" style={{paddingRight: "0", paddingBottom: "30px"}}>
                    <Button variant="outlined" className={ c.buttonG } type="submit" disabled={ props.pristine || props.submitting || props.invalid }>
                        Submit
                    </Button>
                </Col>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddSourceBaseURLForm', // a unique identifier for this form
    validate
})(AddSourceBaseURLForm)
