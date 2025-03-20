# Domain Documentation

This directory contains documentation related to the domain model and ubiquitous language used in the Lookbook application.

## Directory Structure

- `glossary.md` - Defines the ubiquitous language and core concepts
- `aggregates/` - Documentation for each aggregate root and its entities
- `value-objects/` - Documentation for value objects
- `events/` - Documentation for domain events
- `services/` - Documentation for domain services

## Guidelines

1. **Ubiquitous Language**
   - Use consistent terminology across all documentation and code
   - Define terms clearly and unambiguously
   - Document any changes to terminology

2. **Domain Model**
   - Document the relationships between entities
   - Explain invariants and business rules
   - Describe the lifecycle of entities

3. **Bounded Contexts**
   - Define clear boundaries between different parts of the system
   - Document how contexts interact with each other
   - Specify the shared kernel between contexts

4. **Documentation Format**
   - Use markdown for all documentation
   - Include diagrams when helpful (using PlantUML or Mermaid)
   - Keep documentation up to date with code changes 