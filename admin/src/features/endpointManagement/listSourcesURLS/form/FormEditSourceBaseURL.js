import React from 'react'
import {connect} from 'react-redux'
import { Field, reduxForm } from 'redux-form'
import { renderTextField} from "../../../sourcesManagement/addSource/forms/FormElements";
import { Grid } from "@material-ui/core";
import { useStyles} from "../../../sourcesManagement/addSource/forms/FormElements";


let FormEditSourceBaseURL = props => {
    const { handleSubmit  } = props

    const c = useStyles();

    return (
        <form onSubmit={handleSubmit} id="editSourceBaseURL">
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <Field
                        disabled
                        size="small"
                        variant="outlined"
                        name="source"
                        component={renderTextField}
                        label="Label"
                        className={c.field}
                        labelText="Choose the resource associate to the base URL you want to insert. "
                    >
                    </Field>

                </Grid>
                <Grid item xs={12} >
                    <Field
                        size="small"
                        variant="outlined"
                        name="url"
                        component={renderTextField}
                        label="Base URL"
                        className={c.field}
                        labelText="Base URL. It must contains '#replace#'."
                    />
                </Grid>
            </Grid>
        </form>
    )
}

FormEditSourceBaseURL = reduxForm({
    form: 'initializeFromState'  // a unique identifier for this form
})(FormEditSourceBaseURL)

FormEditSourceBaseURL = connect(
    state => ({
        initialValues: state.endpointManagement.editRow // pull initial values from account reducer
    }),
)(FormEditSourceBaseURL)

export default FormEditSourceBaseURL