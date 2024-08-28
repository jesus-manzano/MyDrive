package com.mydrive.backend.services;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Fábrica de servicios de almacenamiento en la nube.
 *
 * <p>Esta clase es responsable de proporcionar instancias de servicios de almacenamiento en la nube
 * basados en el nombre del servicio especificado. Utiliza el {@link BeanFactory} para obtener los
 * beans correspondientes a los servicios de almacenamiento en la nube registrados en el contexto de
 * Spring.</p>
 *
 * <p>La clase está anotada con {@link Service}, lo que indica que es un componente de servicio de
 * Spring y puede ser inyectada en otros componentes como una dependencia.</p>
 *
 * @see CloudStorageService
 */
@Service
public class CloudStorageServiceFactory {

    private final BeanFactory beanFactory;

    /**
     * Crea una nueva instancia de {@link CloudStorageServiceFactory}.
     *
     * <p>Inyecta el {@link BeanFactory} necesario para recuperar los beans de los servicios de
     * almacenamiento en la nube.</p>
     *
     * @param beanFactory El {@link BeanFactory} utilizado para obtener los beans de servicios de
     *                    almacenamiento en la nube.
     */
    @Autowired
    public CloudStorageServiceFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Obtiene una instancia del servicio de almacenamiento en la nube correspondiente al nombre
     * especificado.
     *
     * <p>Utiliza el {@link BeanFactory} para recuperar el bean del servicio de almacenamiento en la
     * nube registrado bajo el nombre proporcionado.</p>
     *
     * @param cloudService El nombre del servicio de almacenamiento en la nube que se desea obtener.
     * @return Una instancia de {@link CloudStorageService} correspondiente al nombre especificado.
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException Si no se encuentra un
     *         bean con el nombre especificado en el contexto de Spring.
     */
    public CloudStorageService getCloudService(String cloudService) {
        return beanFactory.getBean(cloudService, CloudStorageService.class);
    }
}
