import React, {useMemo, useRef, useState} from 'react'
import {connect, useSelector} from 'react-redux'
import { Field, reduxForm } from 'redux-form'
import {
    renderDropzoneInput,
    renderMultipleSelectField,
    renderSelectField,
    renderSwitchField,
    renderTextField
} from "../../addSource/forms/FormElements";
import {
    Chip,
    Divider,
    FormControl, FormLabel,
    Grid,
    Input,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Select,
    Typography
} from "@material-ui/core";
import {
    getConceptsLabels,
    getEntitiesLabels,
    getOrdersLabels, getPluginsLabels,
    getResourcesLabels
} from "../../addSource/addSourceSlice";
import {makeStyles, useTheme} from '@material-ui/core/styles';


function getStyles(name, personName, theme) {
    return {
        fontWeight:
            personName.indexOf(name) === -1
                ? theme.typography.fontWeightRegular
                : theme.typography.fontWeightMedium,
    };
}

const useStyles = makeStyles((theme) => ({
    select: {
        '& .MuiOutlinedInput-input': {
            padding: "13.5px 14px 16.5px 14px"
        }
    },
    chips: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    chip: {
        margin: 2,
    },
    noLabel: {
        marginTop: theme.spacing(3),
    },
}));


let FormEditResource = props => {
    const { handleSubmit, change, initialValues, load, pristine, reset, submitting } = props

    const classes = useStyles();
    const theme = useTheme();

    const baseStyle = {
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: '20px',
        borderWidth: 1,
        borderRadius: 5,
        background: "#000000",
        opacity: 0.8,
        borderStyle: 'solid',
        backgroundColor: '#fafafa',
        color: '#bdbdbd',
        outline: 'none',
        transition: 'border .24s ease-in-out'
    };
    const style = useMemo(() => ({
        ...baseStyle,
    }), []);

    const conceptLabels = useSelector(getConceptsLabels)
    const ordersLabels = useSelector(getOrdersLabels)
    const pluginLabels = useSelector(getPluginsLabels)

    const [publisher, setPublisher] = useState('');
    const [endpoint, setEndpoint] = useState(false);

    return (
        <form onSubmit={handleSubmit} id="editForm">
            <Grid container spacing={2} style={{paddingBottom: "20px"}}>
                <Grid item xs={6}>
                    <Field
                        size="small"
                        variant="outlined"
                        name="title"
                        component={renderTextField}
                        label="Title"
                        labelText="olaaa"
                    />
                </Grid>
                <Grid item xs={6}>
                    <Field
                        disabled
                        size="small"
                        variant="outlined"
                        name="label"
                        component={renderTextField}
                        label="Label"
                        labelText="olaaa"
                    />
                </Grid>

                <Grid item xs={12} >
                    <Field
                        size="small"
                        variant="outlined"
                        name="description"
                        component={renderTextField}
                        label="Description"
                        labelText="olaaa"
                        multiline
                        rows={2}
                    />
                </Grid>

                <Grid item xs={6} >
                    <Field
                        size="small"
                        name="isResourceOf"
                        component={renderSelectField}
                        label="Resource Of"
                        variant="outlined"
                        className={classes.field}
                        labelText="olaaa"
                    >
                        <MenuItem value=""><em>None</em></MenuItem>
                        {
                            Object.keys(conceptLabels).map((key,i) => {
                                return (<MenuItem key={i} value={key}> {conceptLabels[key]} </MenuItem>)
                            })
                        }
                    </Field>
                </Grid>
                <Grid item xs={6} >
                    <Field
                        classes={classes}
                        size="small"
                        name="extends"
                        component={renderSelectField}
                        label="Extends"
                        variant="outlined"
                        className={classes.field}
                        labelText="olaaa"
                    >
                        <MenuItem value=""><em>None</em></MenuItem>
                        {
                            Object.keys(conceptLabels).map((key,i) => {
                                return (<MenuItem key={i} value={key}> {conceptLabels[key]} </MenuItem>)
                            })
                        }
                    </Field>
                </Grid>

                <Grid item xs={6} >
                    <Field
                        classes={classes}
                        size="small"
                        name="order"
                        component={renderSelectField}
                        label="Order"
                        variant="outlined"
                        className={classes.field}
                        labelText="olaaa"
                    >
                        <MenuItem value=""><em>None</em></MenuItem>
                        {ordersLabels.map((option) => (
                            <MenuItem key={option} value={option+""}>
                                {option}
                            </MenuItem>
                        ))}
                    </Field>
                </Grid>
                <Grid item xs={6} >
                    <Field
                        size="small"
                        name="publisherLabel"
                        component={renderSelectField}
                        label="Publisher"
                        onChange={e => setPublisher(e.target.value)}
                        variant="outlined"
                        className={classes.field}
                        labelText="olaaa"
                    >
                        <MenuItem value=""><em>None</em></MenuItem>
                        {pluginLabels.map((option) => (
                            <MenuItem key={option} value={option}>
                                {option}
                            </MenuItem>
                        ))}
                    </Field>
                </Grid>

                {publisher==='OMIM' && (
                    <Grid container spacing={2} >
                        <Grid item xs={6} style={{paddingLeft: "16px"}}>
                            <Field
                                name="genemap"
                                style={style}
                                component={renderDropzoneInput}
                            />
                            <FormLabel style={{ fontSize: "12px", marginLeft: "16px", letterSpacing: "0.0333em", marginTop: "12px" }}>Upload Genemap file</FormLabel>
                        </Grid>
                        <Grid item xs={6} style={{paddingRight: "16px"}}>
                            <Field
                                name="morbidmap"
                                style={style}
                                component={renderDropzoneInput}
                            />
                            <FormLabel style={{ fontSize: "12px", marginLeft: "16px", letterSpacing: "0.0333em", marginTop: "12px" }}>Upload Morbidmap file</FormLabel>
                        </Grid>
                    </Grid>
                )}
                {publisher!=='OMIM' && publisher!== '' && !endpoint && (
                    <Grid container spacing={2} style={{paddingLeft: "8px", paddingRight: "8px"}}>
                        <Grid container justify="flex-end" style={{paddingRight: "16px", paddingTop: "15px"}}>
                            <Field
                                name="isEndpointFile"
                                component={renderSwitchField}
                                size="small"
                                label="Upload local File"
                                className={classes.switch}
                                checked={endpoint}
                                labelPlacement="start"
                                onChange={e => setEndpoint(e.target.checked)}
                                labelText="olaaa"
                                style={{ textAlign: "right" }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <Field
                                size="small"
                                variant="outlined"
                                name="endpointResource"
                                component={renderTextField}
                                label="Endpoint's URL"
                                className={classes.field}
                                labelText="olaaa"
                            />
                        </Grid>
                    </Grid>
                )}
                {publisher!=='OMIM' && publisher!== '' && endpoint && (
                    <Grid container spacing={2} style={{paddingLeft: "8px", paddingRight: "8px"}}>
                        <Grid container spacing={2} justify="flex-end" style={{paddingRight: "20px", paddingTop: "15px"}}>
                            <Field
                                name="isEndpointFile"
                                component={renderSwitchField}
                                labelPlacement="start"
                                label="Upload local File"
                                className={classes.switch}
                                size="small"
                                checked={endpoint}
                                onChange={e => setEndpoint(e.target.checked)}
                                labelText="olaaa"
                            />
                        </Grid>
                        <Grid item xs={12} style={{marginLeft: "16px"}}>
                            <Field
                                name="files"
                                style={style}
                                component={renderDropzoneInput}
                            />
                            <FormLabel style={{ fontSize: "12px", marginLeft: "16px", letterSpacing: "0.0333em", marginTop: "12px" }}>Endpoint</FormLabel>
                        </Grid>
                    </Grid>
                )}
            </Grid>

            <Divider light/>
            <Typography variant="overline" gutterBottom >
                Please note.
            </Typography>
           
        </form>
    )
}


FormEditResource = reduxForm({
    form: 'initializeFromState'  // a unique identifier for this form
})(FormEditResource)


FormEditResource = connect(
    state => ({
        initialValues: state.listResources.editRow // pull initial values from account reducer
    }),
)(FormEditResource)

export default FormEditResource