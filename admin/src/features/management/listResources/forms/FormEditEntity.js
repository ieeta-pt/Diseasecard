import React, {useRef} from 'react'
import {connect, useSelector} from 'react-redux'
import { Field, reduxForm } from 'redux-form'
import {renderMultipleSelectField, renderSelectField, renderTextField} from "../../addSource/forms/FormElements";
import {Chip, FormControl, Grid, Input, InputLabel, MenuItem, OutlinedInput, Select} from "@material-ui/core";
import {getConceptsLabels} from "../../addSource/addSourceSlice";
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

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    PaperProps: {
        style: {
            maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
            width: 250,
        },
    },
};

let FormEditEntity = props => {
    const { handleSubmit, change, initialValues, load, pristine, reset, submitting } = props

    const classes = useStyles();
    const theme = useTheme();

    const conceptsLabels = useSelector(getConceptsLabels)

    const [isEntityOfLabel, setIsEntityOfLabel] = React.useState(initialValues.isEntityOfLabel);

    const handleChange = (event) => {
        setIsEntityOfLabel(event.target.value);
        change("isEntityOfLabel", event.target.value);
    };

    const labelRef = useRef()
    const labelWidth = labelRef.current ? labelRef.current.clientWidth : 0

    return (
        <form onSubmit={handleSubmit} id="editForm">
            <Grid container spacing={2}>
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

                <Grid item xs={12}>
                    <FormControl variant="outlined" style={{width: "100%"}}>
                        <InputLabel id="demo-mutiple-chip-label" ref={labelRef}>Extended Concepts</InputLabel>
                        <Select
                            className={classes.select}
                            labelId="demo-mutiple-chip-label"
                            id="demo-mutiple-chip"
                            multiple
                            value={props?.value ? props.value : isEntityOfLabel}
                            onChange={handleChange}
                            variant="outlined"
                            MenuProps={MenuProps}
                            name="isEntityOfLabel"
                            input={<OutlinedInput aria-describedby="my-helper-text" labelWidth={labelWidth} name="isEntityOfLabel"  id="select-multiple-chip" />}
                            renderValue={(selected) => (
                                <div className={classes.chips}>
                                    {selected.map((value) => (
                                        <Chip key={value} label={value} className={classes.chip} />
                                    ))}
                                </div>
                            )}
                        >
                            {Object.keys(conceptsLabels).map((key,i) => (
                                <MenuItem key={conceptsLabels[key]} value={conceptsLabels[key]} style={getStyles(conceptsLabels[key], isEntityOfLabel, theme)}>
                                    {conceptsLabels[key]}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Grid>


            </Grid>
        </form>
    )
}

// Decorate with reduxForm(). It will read the initialValues prop provided by connect()
FormEditEntity = reduxForm({
    form: 'initializeFromState'  // a unique identifier for this form
})(FormEditEntity)

// You have to connect() to any reducers that you wish to connect to yourself
FormEditEntity = connect(
    state => ({
        initialValues: state.listResources.editRow // pull initial values from account reducer
    }),
)(FormEditEntity)

export default FormEditEntity